(ns expense-tracker.web.handler
  (:require
   [integrant.core :as ig]
   [muuntaja.core :as m]
   [reitit.ring :as ring]
   [reitit.ring.middleware.muuntaja :as muuntaja]))

(defmethod ig/init-key :handler/ring
  [_ {:keys [router]}]
   (ring/ring-handler router (ring/create-default-handler))
  )

(defmethod ig/init-key :router/core
  [_ {:keys [routes]}]
  (ring/router
   routes
   {:data {:muuntaja m/instance
           :middleware [muuntaja/format-middleware]}}))
