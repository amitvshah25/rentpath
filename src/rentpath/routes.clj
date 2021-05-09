(ns rentpath.routes
  (:require [rentpath.event-source :refer [user-events]]
            [ring.util.response :refer [response]]))

(def event-points [{:type "PushEvent" :points 5}
                   {:type "PullRequestReviewCommentEvent" :points 4}
                   {:type "WatchEvent" :points 3}
                   {:type "CreateEvent" :points 2}])

(defn event->score [event-type]
  (if-let [points (->> event-points
                       (filter #(= event-type (:type %)))
                       (first)
                       (:points))]
    points
    1)) ;1 for all other events

(defn score-aggregator [{score :score :as result}
                        {type :type id :id login :login :as event}]
  {:id id :login login :score (+ score (event->score type))})

(defn aggr-all-scores [all-events]
  (->> all-events
       (group-by :id)
       (vals)
       (map #(reduce score-aggregator {:score 0} %))))

(defn scores [id]
  (let [events-to-aggr (if (nil? id)
                         @user-events
                         (filter #(= (str (:id %)) id) @user-events))]
    (-> events-to-aggr
        (aggr-all-scores)
        (response))))
