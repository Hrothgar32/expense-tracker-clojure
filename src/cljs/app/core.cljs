(ns app.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(def options ["USD" "RON" "EUR"])

;; Re-frame part of the app

 ;; Effects
(rf/reg-event-fx
 :app/initialize
 (fn [_ _]
   {:db {:transactions/transactions []}}))

;; Subscriptions

(rf/reg-sub
 :form/fields
 (fn [db _]
   (:form/fields db)))

(rf/reg-sub
 :form/field
 :<- [:form/fields]
 (fn [fields [_ id]]
   (get fields id)))

;; Events

(rf/reg-event-db
 :form/set-field
 [(rf/path :form/fields)]
 (fn [fields [_ id value]]
   (assoc fields id value)))


;; Components

(defn transaction-list []
  [:div.bg-red.shadow-md.rounded.mb-2
   [:h1.font-bold "Transactions"]
   [:ul
    ]])

(defn text-input [{:keys [value attributes on-save]}]
  (let [draft (r/atom nil)
        result (r/track #(or @draft @value ""))]
    (fn []
     [:input.border.rounded.w-full
      (merge attributes {:type :number
                         :on-focus #(reset! draft (or @value ""))
                         :on-blur (fn []
                                    (on-save (or @draft ""))
                                    (reset! draft nil))
                         :on-change #(reset! draft (.. % -target -value))
                         :value @result})])))

(defn select-input [{:keys [value options attributes on-save]}]
  (let [draft (r/atom nil)
        result (r/track #(or @draft @value ""))]
    (fn []
     [:select.border
      (merge attributes {:on-focus #(reset! draft (or @value ""))
                         :on-blur (fn []
                                    (on-save (or @draft ""))
                                    (reset! draft nil))
                         :on-change #(reset! draft (.. % -target -value))
                         :value @result})
      (map #(do [:option %]) options)])))

(defn form-component []
  [:form.bg-white.shadow-md.rounded.px-8.pb-1.w-full
   {:on-submit #()}
   [:div.mb-4
    [:label.block.m-auto "Amount owed"]
    [:div.flex.gap-2
     [text-input {:attributes {:name :amount}
                  :value (rf/subscribe [:form/field :name])
                  :on-save #(rf/dispatch [:form/set-field :name %])}]
     [select-input {:attributes {:name :currency}
                    :options options
                    :value (rf/subscribe [:form/field :currency])
                    :on-save #(rf/dispatch [:form/set-field :currency %])
                    }
      ]]
    [:button.bg-green-500.hover:bg-green-700.text-white.font-bold.rounded.p-2.mt-3 {:type "submit"}
     "Submit amount"]]])

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (rdom/render [:div.w-full.max-w-xl.m-auto
                [transaction-list]
                [form-component]] (js/document.querySelector "#app")))

(defn init! []
  (mount-root)
  (rf/dispatch [:app/initialize]))

(comment
  (print "Hello"))
