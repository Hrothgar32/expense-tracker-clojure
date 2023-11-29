(ns expense-tracker.web.handler
  (:require
   [integrant.core :as ig]
   [reitit.ring :as ring]))

(defmethod ig/init-key :handler/ring
  [_ {:keys [router]}]
   (ring/ring-handler router (ring/create-default-handler))
  )

(defmethod ig/init-key :router/core
  [_ {:keys [routes]}]
  (ring/router
   routes))
