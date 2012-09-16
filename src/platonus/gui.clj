(ns platonus.gui
  (:use [seesaw chooser
                 core])
  (:require [clojure.string :as string]
            [platonus.filesystem :as filesystem]
            [platonus.network :as network]
            [platonus.uiagent :as uiagent]))

(defn- load-file-action
  [model]
  (fn [_]
    (let [file     (choose-file)
          encoding (input "Encoding:")
          nickname (input "Nickname:")]
      (uiagent/transform-network model
        (fn [network]
          (filesystem/scan-file network nickname file encoding))))))

(defn- generate-action
  [model]
  (fn [_]
    (uiagent/with-network model
      (fn [network]
        (alert (string/join " " (network/generate network)))))))

(defn- create-file-menu
  [model]
  (menu :text  "File"
        :items [(action :name    "Load..."
                        :handler (load-file-action model))]))

(defn- create-network-menu
  [model]
  (menu :text  "Network"
        :items [(action :name "Generate..."
                        :handler (generate-action model))]))

(defn- create-menubar
  [model]
  (menubar :items [(create-file-menu model)
                   (create-network-menu model)]))

(defn- create-statusbar
  [model]
  (let [label (label "Ready")]
    (uiagent/add-change-listener model
     (fn [network]
       (invoke-now 
         (config! label :text (str "Records: " (count (:network network)))))))
    label))

(defn- create-main-panel
  [model]
  (border-panel :north (create-menubar model)
                :south (create-statusbar model)))

(defn- create-frame
  [model]
  (frame :title    "Platonus"
         :content  (create-main-panel model)
         :on-close :exit))

(defn publish-error
  [^Throwable error]
  (.printStackTrace error))

(defn -main
  [& args]
  (let [model (uiagent/create)]
    (uiagent/set-error-listener model
      (fn [_ error]
        (publish-error error)))
    (invoke-later
      (-> model
          create-frame
          pack!
          show!))))
