(ns expense-tracker.core
  (:require
   [integrant.core :as ig]
   ;; Edges
   [expense-tracker.web.routes.api]
   [expense-tracker.server.core]
   [expense-tracker.web.handler])
  (:gen-class))

(defonce system (atom nil))

(defn start-app []
  (->> (ig/read-string (slurp "resources/system.edn"))
       (ig/init)
       (reset! system)))

(comment
  "This is a string")

(defn stop-app []
  (ig/halt! @system))

(defn -main [& _]
  (start-app))

(comment
  (start-app)
  (stop-app)
  )
