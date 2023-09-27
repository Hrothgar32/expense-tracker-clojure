(ns expense-tracker.web.handler
  (:require
   [integrant.core :as ig]))

(defmethod ig/init-key :handler/ring
  [_ _]
  (fn [req]
    {:status 200
     :body "OK"})
  )
