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

(def local-url (str "http://localhost:"
                    (:server-port conf)
                    "/scores"))

(deftest scores-all-users
  (testing "Scores for all users"
    (is (= (count (make-request-and-parse-body local-url)) 5)))) ;We should get 5 scores for 5 users here.

(deftest scores-by-user
  (testing "Scores for one user"
    (let [valid-usr (str local-url "/5148647")
          invalid-usr (str local-url "/111111")]
      (is (>=  (-> (make-request-and-parse-body valid-usr)
                  (:score))
              0))
      (is (contains?
           (make-request-and-parse-body invalid-usr)
           :error)))))
