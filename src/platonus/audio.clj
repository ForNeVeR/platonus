(ns platonus.audio
  (:require [clojure.java.io :as io]
            [platonus.network :as network])
  (:import [javax.sound.sampled AudioFormat
                                AudioFormat$Encoding
                                AudioSystem
                                DataLine
                                DataLine$Info
                                SourceDataLine]
           [javazoom.spi.mpeg.sampled.file MpegAudioFileReader]))

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

(defn- ^SourceDataLine get-source-data-line
  [format]
  (let [info (DataLine$Info. SourceDataLine format)
        line (AudioSystem/getLine info)]
    (.open line)
    line))

(defn- play-bytes
  [^SourceDataLine line
   bytes
   size]
  (loop [written 0]
    (println "written" written "bytes")
    (if (< written size)
      (let [i (.write line bytes written (- size written))]
        (recur (+ written i))))))

(defn- play-stream
  [stream format]
  (with-open [line (get-source-data-line format)]
    (let [data (byte-array 4096)]
      (.start line)
      (loop []
        (let [bytes-read (.read stream data 0 (alength data))]
          (if (not (= bytes-read -1))
            (do
              (play-bytes line data bytes-read)
              (recur))))))
    ;; Stop:
    (.drain line)
    (.stop line)))

(defn do-with-decoded-stream
  [filename f]
  (let [file (io/file filename)]
    (with-open [input-stream   (open-stream file)]
      (let [decoded-format (get-decoded-format input-stream)]
        (with-open [decoded-stream (decode-stream input-stream decoded-format)]
          (f decoded-stream decoded-format))))))

(defn play-file
  [filename]
  (do-with-decoded-stream filename play-stream))

(defn- get-stream-byte-array
  [stream frame-size frame-count]
  (let [size  (* frame-count frame-size)
        bytes (byte-array size)]
    (println "input size" size)
    (.read stream bytes 0 size)
    bytes))

(defn- get-file-properties
  [filename]
  (let [file (io/file filename)
        reader (MpegAudioFileReader.)
        format (.getAudioFileFormat reader file)]
    (.properties format)))

(defn create-network-from-file
  [filename]
  (let [properties  (get-file-properties filename)
        frame-size  (.get properties "mp3.framesize.bytes")
        frame-count (.get properties "mp3.length.frames")]
    (do-with-decoded-stream filename (fn [stream format]
      (let [bytes  (get-stream-byte-array stream frame-size frame-count)
            frames (->> bytes
                        (partition frame-size)
                        (map seq))]
        (-> (network/create 2 seq)
            (network/add-phrase frames)
            (assoc :format format)))))))

(defn concat-frames
  [frames]
  (apply concat frames))

(defn play-byte-seq
  [bytes format]
  (let [data (byte-array bytes)]
    (println "byte size" (alength data))
    (with-open [line (get-source-data-line format)]
      (play-bytes line data (alength data)))))
