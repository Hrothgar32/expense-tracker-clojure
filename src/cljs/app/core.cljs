(ns app.core
  (:require [reagent.dom :as rdom]
            [day8.re-frame.http-fx]
            [reagent.core :as r]
            [ajax.core :as ajax]
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

(rf/reg-event-fx
 :get-recipe
 (fn [db]
   {
    :http-xhrio {
                 :method :get
                 :uri "http://google.com"
                 :response-format (ajax/text-response-format)
                 :params (get (:form/fields db) :name)
                 }
    }))

(rf/reg-event-db
  :success-http-result
  (fn [db [_ result]]
    (assoc db :success-http-result result)))

(rf/reg-event-db
  :failure-http-result
  (fn [db [_ result]]
    ;; result is a map containing details of the failure
    (assoc db :failure-http-result result)))

;; Components


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
   {:on-submit (fn [e]
                 (.preventDefault e)
                 (rf/dispatch [:get-recipe]))}
   [:div.mb-4
    [:label.block.m-auto "Link"]
    [:div.flex.gap-2
     [text-input {:attributes {:name :amount}
                  :value (rf/subscribe [:form/field :name])
                  :on-save #(rf/dispatch [:form/set-field :name %])}]
     ]
    [:button.bg-green-500.hover:bg-green-700.text-white.font-bold.rounded.p-2.mt-3 {:type "submit"}
     "Submit amount"]]])

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (rdom/render [:div.w-full.max-w-xl.m-auto
                [form-component]] (js/document.querySelector "#app")))

(defn init! []
  (mount-root)
  (rf/dispatch [:app/initialize]))

(comment
  (print "Hello"))
