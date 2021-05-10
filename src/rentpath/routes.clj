(ns rentpath.routes
  (:require [rentpath.event-source :refer [user-scores]]
            [rentpath.github-users :refer [some-github-users]]
            [ring.util.response :refer [response]]))

(defn user->score [accum {id :id :as user}]
  (conj accum (assoc user :score (get @user-scores (keyword (str id))))))

(defn scores
  ([]
   (->> some-github-users
        (reduce user->score [])
        (response)))
  ([id]   
   (as-> some-github-users $
     (filter #(= (str (:id %)) id) $)
     (first $)
     (assoc $ :score (get @user-scores (keyword (str id))))
     (response $))))
