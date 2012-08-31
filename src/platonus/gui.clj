(ns platonus.gui
  (:use [seesaw chooser
                 core])
  (:require [clojure.string :as string]
            [platonus.filesystem :as filesystem]
            [platonus.network :as network]))

(defn- load-file-action
  [network]
  (fn [_]
    (let [file     (choose-file)
          encoding (input "Encoding:")
          nickname (input "Nickname:")]
      (swap! network filesystem/scan-file nickname file encoding))))

(defn- generate-action
  [network]
  (fn [_]
    (alert (string/join " " (network/generate @network)))))

(defn- create-file-menu
  [network]
  (menu :text  "File"
        :items [(action :name    "Load..."
                        :handler (load-file-action network))]))

(defn- create-network-menu
  [network]
  (menu :text "Network"
        :items [(action :name "Generate..."
                        :handler (generate-action network))]))

(defn- create-menubar
  [network]
  (menubar :items [(create-file-menu network)
                   (create-network-menu network)]))

(defn- create-statusbar
  [network]
  (label "Ready"))

(defn- create-main-panel
  [network]
  (border-panel :north (create-menubar network)
                :south (create-statusbar network)))

(defn- create-frame
  [network]
  (frame :title    "Platonus"
         :content  (create-main-panel network)
         :on-close :exit))

(defn -main
  [& args]
  (let [network (atom (network/create))]
    (invoke-later
      (-> network
          create-frame
          pack!
          show!))))
