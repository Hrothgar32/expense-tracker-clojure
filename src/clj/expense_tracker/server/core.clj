(ns expense-tracker.server.core
  (:require
   [clojure.tools.logging :as logging]
   [integrant.core :as ig]
   [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key :server/http [_ {:keys [handler port]}]
  (let [handler (atom handler)]
    (logging/info "Server has started on port:" port)
    (jetty/run-jetty (fn [req] (@handler req)) {:port port :join? false})))

(defmethod ig/halt-key! :server/http [_ server]
  (logging/info "HTTP server stopped")
  (.stop server))
