(ns rentpath.event-source
  (:require [mount.core :refer [defstate]]
            [rentpath.github-users :refer [some-github-users]]
            [clojure.core.async :refer [go-loop >! <! chan sliding-buffer close!]]))

(def all-events ["CommitCommentEvent" "CreateEvent" "DeleteEvent"
                 "ForkEvent" "GollumEvent" "IssueCommentEvent" "IssuesEvent"
                 "MemberEvent" "PublicEvent" "PullRequestEvent" "PullRequestReviewEvent"
                 "PullRequestReviewCommentEvent" "PushEvent" "ReleaseEvent"
                 "SponsorshipEvent" "WatchEvent"])

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

(defn random-event []
  (assoc (rand-nth some-github-users) :type (rand-nth all-events)))

(def user-scores (atom (->> some-github-users
                            (reduce (fn [accum u]
                                      (assoc accum (keyword (str (:id u))) 0)) ;Initialize all the scores to 0
                                    {}))))

(def event-channel
  "Sliding buffer to put some more weight on latest events, dropping the older events"
  (chan (sliding-buffer 10)))

(defstate event-producer
  :start (go-loop []
           (Thread/sleep (* (rand-int 5) 1000)) ;simulate events at random intervals
           (when (>! event-channel (random-event)) ;stop the producer when the channel closes
             (recur)))
  :stop (close! event-channel)) ;close the channel when application is shutdown

(defstate event-consumer
  :start (go-loop []
           (Thread/sleep 1000)
           (when-let [e (<! event-channel)]
             (let [{id :id
                    type :type} e
                   s (event->score type)]
               (swap! user-scores update-in [(keyword (str id))] #(+ % s))
               (recur))))) ;nothing to do on stop. <! will return nil and it will exit out of the go-loop
