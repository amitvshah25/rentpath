(ns rentpath.event-source
  (:require [mount.core :refer [defstate]]
            [rentpath.github-users :refer [some-github-users]]
            [clojure.core.async :refer [go-loop >! <! chan sliding-buffer close!]]))

(def all-events ["CommitCommentEvent" "CreateEvent" "DeleteEvent"
                 "ForkEvent" "GollumEvent" "IssueCommentEvent" "IssuesEvent"
                 "MemberEvent" "PublicEvent" "PullRequestEvent" "PullRequestReviewEvent"
                 "PullRequestReviewCommentEvent" "PushEvent" "ReleaseEvent"
                 "SponsorshipEvent" "WatchEvent"])

(defn random-event []
  (assoc (rand-nth some-github-users) :type (rand-nth all-events)))

(def user-events (atom (repeatedly 500 #(random-event))))

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
             (swap! user-events conj e)
             (recur)))) ;nothing to do on stop. <! will return nil and it will exit out of the go-loop
