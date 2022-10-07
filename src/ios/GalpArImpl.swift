@objc(GalpArImpl) class GalpArImpl : CDVPlugin {
  
  @objc(openObject:)
  func openObject(_ command: CDVInvokedUrlCommand) {
      var pluginResult = CDVPluginResult(
          status: CDVCommandStatus_OK
      )
            
      if(command.methodName == "openObject") {
          let storyboard = UIStoryboard(name: "GalpArMain", bundle: nil)
          let viewController = storyboard.instantiateViewController(withIdentifier: "mainView") as! GalpArViewController
          let jsonData = command.arguments[0] as! NSDictionary
          
          if let resource = jsonData.value(forKey: "resourcePath") as? String {
              viewController.resource = resource
              self.viewController.present(viewController, animated: true)
              self.commandDelegate!.send(
                pluginResult,
                callbackId: command.callbackId
              )
          }
      } else {
          pluginResult = CDVPluginResult(
              status: CDVCommandStatus_ERROR
          )
          self.commandDelegate!.send(
            pluginResult,
            callbackId: command.callbackId
            
          )
      }
  }
}
