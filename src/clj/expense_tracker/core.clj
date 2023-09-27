(ns expense-tracker.core
  (:require
   [integrant.core :as ig]
   ;; Edges
   [expense-tracker.database.core]
   [expense-tracker.server.core]
   [expense-tracker.web.handler])
  (:gen-class))

(defonce system (atom nil))

(defn start-app []
  (->> (ig/read-string (slurp "resources/system.edn"))
       (ig/init)
       (reset! system)))

(defn stop-app []
  (ig/halt! @system))

(defn -main [& _]
  (start-app))

(comment
  (start-app)
  (stop-app)
  )
