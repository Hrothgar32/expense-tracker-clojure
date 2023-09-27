(ns expense-tracker.database.core
  (:require
   [clojure.java.io :as io]
   [clojure.tools.logging :as logging]
   [integrant.core :as ig]
   [xtdb.api :as xt]))

(defmethod ig/init-key :db/xtdb
  [_ {:keys [tx-log document-store index-store]}]
  (logging/info "XTDB node has started!")
  (letfn [(kv-store [dir]
            {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                        :db-dir (io/file dir)
                        :sync? true}})]
    (xt/start-node
     {:xtdb/tx-log (kv-store tx-log)
      :xtdb/document-store (kv-store document-store)
      :xtdb/index-store (kv-store index-store)})))

(defmethod ig/halt-key! :db/xtdb
  [_ node]
  (logging/info "XTDB node has been stopped!")
  (.close node))
