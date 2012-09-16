(ns platonus.audio
  (:require [clojure.java.io :as io])
  (:import [javax.sound.sampled AudioFormat
                                AudioFormat$Encoding
                                AudioSystem
                                DataLine
                                DataLine$Info
                                SourceDataLine]))


(defn- open-stream
  [file]
  (AudioSystem/getAudioInputStream file))

(defn- get-stream-format
  [stream]
  (.getFormat stream))

(defn- get-decoded-format
  [input-stream]
  (let [input-format (get-stream-format input-stream)]
    (AudioFormat. AudioFormat$Encoding/PCM_SIGNED
                  (.getSampleRate input-format)
                  16
                  (.getChannels input-format)
                  (* (.getChannels input-format) 2)
                  (.getSampleRate input-format)
                  false)))

(defn- decode-stream
  [input-stream decoded-format]
  (let [input-format   (get-stream-format input-stream)]
    (AudioSystem/getAudioInputStream decoded-format input-stream)))

(defn- get-source-data-line
  [format]
  (let [info (DataLine$Info. SourceDataLine format)
        line (AudioSystem/getLine info)]
    (.open line)
    line))

(defn- play-stream
  [stream format]
  (with-open [line (get-source-data-line format)]
    (let [data (byte-array 4096)]
      (.start line)
      (loop []
        (let [bytes-read (.read stream data 0 (.length data))]
          (if (not (= bytes-read -1))
            (do
              (.write line data 0 bytes-read)
              (recur))))))
    ;; Stop:
    (.drain line)
    (.stop line)))

(defn play-file
  [filename]
  (let [file (io/file filename)]
    (with-open [input-stream   (open-stream file)
                decoded-format (get-decoded-format input-stream)
                decoded-stream (decode-stream input-stream)]
      (play-stream decoded-stream decoded-format))))
