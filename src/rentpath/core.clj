(ns rentpath.core
  (:require [mount.core :as mc]
            [rentpath.config :as config]
            [rentpath.github-users :as rg]
            [rentpath.event-source :as re]
            [rentpath.web :as rw])
  (:gen-class))

(defn start-app []
  (mc/start))

(defn stop-app []
  (mc/stop))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
