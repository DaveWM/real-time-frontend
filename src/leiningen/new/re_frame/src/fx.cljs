(ns {{ns-name}}.fx
  (:require [re-frame.core :as re-frame]
            [re-frame.fx :refer [reg-fx]]
            [{{ns-name}}.socket :as socket]))

(reg-fx
  :socket/subscribe
  (fn [evt]
    (doseq [{:keys [sub on-complete]} evt]
      (socket/send! [:event/subscribe sub]
                    5000
                    #(when on-complete
                       (re-frame/dispatch [on-complete %]))))))

(reg-fx
  :socket/unsubscribe
  (fn [evt]
    (doseq [{:keys [sub on-complete]} evt]
      (socket/send! [:event/unsubscribe sub] 5000 #(when on-complete
                                                     (re-frame/dispatch [on-complete %]))))))

(reg-fx
  :socket/send-event
  (fn [{:keys [event data]}]
    (socket/send! [event data])))
