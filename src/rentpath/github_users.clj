(ns rentpath.github-users
  (:require [clj-http.client :as hc]
            [rentpath.config :refer [conf]]))

(defn get-my-user-id
  "Returns the user-id for my-github-user from resources/config.edn"
  [{github-url :github-url
    my-github-user :my-github-user}]  
  (let [get-user-url (str github-url "/users/" my-github-user)]
    (-> get-user-url
        (hc/get {:as :json})
        (get-in [:body :id]))))

(defn users-after-me
  "Returns 5 users who signed up after my-github-user from resources/config.edn"
  [{github-url :github-url}
   my-user-id]  
  (let [get-users-url (str github-url "/users")]
     (->> (hc/get get-users-url {:query-params {:since my-user-id :per_page 5}
                                 :as :json})
          (:body)
          (map #(select-keys % [:id :login])))))

(def some-github-users 
  (->> (get-my-user-id conf)
       (users-after-me conf)))
