(ns nginx.clojure.rewrite-handler-for-test
  (:use [nginx.clojure.core])
  (:use [ring.middleware.session])
  (:use [ring.middleware.session.cookie]))

(def my-session-store (cookie-store {:key "my-secrect-key!!"}))

(def ws (wrap-session 
   (fn [{session :session}]
     {:role (:user session "guest") :body ""}) 
     {:store my-session-store}))

(defn compute-user-role [req]
  (:role (ws req)))

(defn speed-limiter [req]
  (if (= "VIP" (compute-user-role req))
    (set-ngx-var! req "limit_rate" "200k")
    (set-ngx-var! req "limit_rate" "10k"))
  phrase-done)

(defn headers-more [req]
  (let [headers (:headers req)]
    (dissoc! headers "OK")
    (dissoc! headers "User-Agent")
    ;(assoc!  headers "User-Agent" "")
    (dissoc!  headers "Accept-Encoding")
    (assoc!  headers "Accept-Encoding" "gzip")
    (assoc!  headers "jwt-token"  "Good!")
    phrase-done))

