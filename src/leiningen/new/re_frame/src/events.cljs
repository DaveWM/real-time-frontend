(ns {{ns-name}}.events
  (:require
   [re-frame.core :as re-frame]{{#re-pressed?}}
   [re-pressed.core :as rp]{{/re-pressed?}}
   [{{ns-name}}.db :as db]{{#10x?}}
   [day8.re-frame.tracing :refer-macros [fn-traced]]{{/10x?}}
   [{{ns-name}}.cofx :as cofx]
   ))

(re-frame/reg-event-db
 ::initialize-db
 ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [_ _]
   db/default-db))
{{#routes?}}

(re-frame/reg-event-fx
  ::navigate
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel){{#re-pressed?}}
    :dispatch [::rp/set-keydown-rules
               {:event-keys [[[::set-re-pressed-example "Hello, world!"]
                              [{:keyCode 72} ;; h
                               {:keyCode 69} ;; e
                               {:keyCode 76} ;; l
                               {:keyCode 76} ;; l
                               {:keyCode 79} ;; o
                               ]]]
                :clear-keys
                [[{:keyCode 27} ;; escape
                  ]]}]{{/re-pressed?}}}))
{{/routes?}}
{{#re-pressed?}}

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))
{{/re-pressed?}}

(defmulti update-db (fn [_ {[sub-type] :sub}] sub-type))

(defmethod update-db :counter [db {counter :data}]
  (assoc db :counter (:counter/value counter)))

(defmethod update-db :default [_ _] nil)

(re-frame/reg-event-db
  :server/push
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [db [_ evt]]
    (update-db db evt)))

(re-frame/reg-event-db
  ::counter-received
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [db [_ {counter :data}]]
   (assoc db :counter (:counter/value counter))))

(re-frame/reg-event-fx
  ::increment-counter
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db]} _]
    {:db                db
     :socket/send-event {:event :counter/increment}}))

(re-frame/reg-event-fx
  ::decrement-counter
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db]} _]
    {:db                db
     :socket/send-event {:event :counter/decrement}}))

(re-frame/reg-event-fx
  ::socket-open
  [(re-frame/inject-cofx ::cofx/user-token)]
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db user-token] :as xs} [_ active-page params]]
    (merge-with concat
                {:db (assoc db :socket-connected? true)}
                {:socket/subscribe [{:sub [:counter]
                                     :on-complete ::counter-received}]})))

(re-frame/reg-event-fx
  ::socket-closed
  ({{^10x?}}fn{{/10x?}}{{#10x?}}fn-traced{{/10x?}} [{:keys [db]} _]
    {:db (assoc db :socket-connected? false)}))
