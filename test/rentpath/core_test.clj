(ns rentpath.core-test
  (:require [clojure.test :refer :all]
            [rentpath.core :refer :all]
            [rentpath.config :refer [conf]]
            [rentpath.github-users :refer [some-github-users]]            
            [clj-http.client :as hc]))

(defn rentpath-fixture [rentpath-tests]
  (start-app)
  (rentpath-tests)
  (stop-app))

(use-fixtures :once rentpath-fixture)

(deftest github-users
  (testing "Github users initialization"
    (is (> (count some-github-users) 0)))) ;We have initialized it to 5 users

(defn make-request-and-parse-body [url]
  (->  (hc/get url {:as :json})
       (:body)))

(deftest scores-all-users
  (testing "Scores for all users"
    (let [local-url "http://localhost:"
          local-url (str local-url (:server-port conf))
          local-url (str local-url "/scores")]
      (is (= (count (make-request-and-parse-body local-url)) 5))))) ;We should get 5 scores for 5 users here.

(deftest scores-by-user
  (testing "Scores for one user"
    (let [local-url "http://localhost:"
          local-url (str local-url (:server-port conf))
          local-url (str local-url "/scores/5148647")] ;Checking for a given user. 
      (is (>  (-> (make-request-and-parse-body local-url)
                  (first)
                  (:score))
              0)))))
