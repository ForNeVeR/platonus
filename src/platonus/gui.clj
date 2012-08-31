(ns platonus.gui
  (:use seesaw.core))

(defn- load-file-action
  [arg]
  (alert "Placeholder."))

(defn- create-file-menu
  []
  (menu :text  "File"
        :items [(action :name    "Load..."
                        :handler load-file-action)]))

(defn- create-menubar
  []
  (menubar :items [(create-file-menu)]))

(defn- create-frame
  []
  (frame :title    "Platonus"
         :content  (create-menubar)
         :on-close :exit))

(defn -main
  [& args]
  (invoke-later
    (-> (create-frame)
        pack!
        show!)))
