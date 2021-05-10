(ns rentpath.web
  (:require [org.httpkit.server :refer [run-server]]
            [mount.core :refer [defstate]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.middleware.json :refer [wrap-json-response]]
            [rentpath.routes :refer [scores]]
            [rentpath.config :refer [conf]]))

(defroutes all-routes
  (GET "/scores" [] (scores))
  (GET "/scores/:id" [id] (scores id))
  (not-found "Page not found"))

(def app (-> all-routes
             (wrap-json-response)))

(defstate webserver
  :start (run-server app {:port (:server-port conf)})
  :stop (webserver :timeout (:stop-timeout conf)))
