package org.opendatakit.aggregate.gradle.gae

import org.gradle.api.tasks.JavaExec

import java.util.concurrent.Callable

class Update extends JavaExec {
  def email

  String getEmail() {
    if (email == null)
      throw new RuntimeException("email must be configured")

    if (email instanceof Callable)
      email.call()
    else
      email.toString()
  }

  String getSDKHome() {
    return "${getWorkingDir()}/appengine-java-sdk-1.9.54".toString()
  }

  String getEarPath() {
    return "${getWorkingDir()}/ear".toString()
  }

  @Override
  void exec() {
    classpath("${getSDKHome()}/lib/appengine-tools-api.jar")
    setMain("com.google.appengine.tools.admin.AppCfg")
    setArgs([
        "--oauth2",
        "--noisy",
        "--sdk_root=${getSDKHome()}".toString(),
        "--email=${getEmail()}".toString(),
        "--use_google_application_default_credentials",
        "update",
        getEarPath()
    ])
    setStandardInput(System.in)
    setStandardOutput(System.out)
    setErrorOutput(System.err)
    super.exec()
  }

}