Publishing
----------

To publish the project to Bintray / JCenter, just follow the [bintray
documentation][bintray-docs]:

1.  Create `~/.bintray/.credentials` file such as this:

        realm = Bintray API Realm
        host = api.bintray.com
        user = username
        password = key which you can get in your profile under API Key

2.  Execute `sbt publish`.

[bintray-docs]: http://szimano.org/automatic-deployments-to-jfrog-oss-and-bintrayjcentermaven-central-via-travis-ci-from-sbt/#Provide_credentials
