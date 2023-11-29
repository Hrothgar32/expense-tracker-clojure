(ns app.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]))

(def options ["USD" "RON" "EUR"])

(defonce transactions (r/atom (vector)))

(defn transaction-list []
  [:div.bg-red.shadow-md.rounded.mb-2
   [:h1.font-bold "Transactions"]
   [:ul
    ]])

(defn form-submit [event]
  (.preventDefault event)
  (js/console.log "Hell yeah")
  ())

(defn form-component []
  [:form.bg-white.shadow-md.rounded.px-8.pb-1.w-full
   {:on-submit form-submit}
   [:div.mb-4
    [:label.block.m-auto "Amount owed"]
    [:div.flex.gap-2
     [:input.border.rounded.w-full {:type "text"
                                    :name "amount"}]
     [:select.border {:name "currency"}
      (map #(do
              [:option %]) options)]]
    [:button.bg-green-500.hover:bg-green-700.text-white.font-bold.rounded.p-2.mt-3 {:type "submit"}
     "Submit amount"]]])

(defn ^:dev/after-load init []
  (rdom/render [:div.w-full.max-w-xl.m-auto
                [transaction-list]
                [form-component]] (js/document.querySelector "#app")))

(comment
  (print "Hello"))
