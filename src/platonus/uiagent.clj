(ns platonus.uiagent
  (:require [platonus.network :as network]))

(defn create
  []
  (agent {:network   (network/create-default)
          :on-change []}))

(defn add-change-listener
  [uiagent f]
  (send uiagent
    (fn [state]
      (assoc state :on-change
        (conj (:on-change state) f)))))

(defn set-error-listener
  [uiagent f]
  (set-error-handler! uiagent f))

(defn transform-network
  [uiagent f]
  (send uiagent
    (fn [state]
      (let [network (f (:network state))]
        (doseq [listener (:on-change state)]
          (listener network))
        (assoc state :network network)))))

(defn with-network
  [uiagent f]
  (f (:network @uiagent)))
