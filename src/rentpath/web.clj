(ns rentpath.web
  (:require [org.httpkit.server :refer [run-server]]
            [mount.core :refer [defstate]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.middleware.json :refer [wrap-json-response]]
            [rentpath.routes :refer [scores]]
            [rentpath.config :refer [conf]]))

(defroutes all-routes
  (GET "/scores" [] scores)
  (not-found "Page not found"))

(def app (-> all-routes
             (wrap-json-response)
             (wrap-keyword-params)
             (wrap-params)))

(defstate webserver
  :start (run-server app {:port (:server-port conf)})
  :stop (webserver :timeout (:stop-timeout conf)))
