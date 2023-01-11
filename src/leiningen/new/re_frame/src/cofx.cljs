(ns {{ns-name}}.cofx
  (:require [{{ns-name}}.auth :as auth]
            [re-frame.cofx :refer [reg-cofx]]))

(reg-cofx
  ::user-token
  (fn [coeffects _]
    (assoc coeffects :user-token (auth/get-token))))
