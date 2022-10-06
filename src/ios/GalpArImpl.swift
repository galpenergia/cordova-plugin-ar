@objc(GalpArImpl) class GalpArImpl : CDVPlugin {
  
  @objc(openObject:)
  func openObject(_ command: CDVInvokedUrlCommand) {
    var pluginResult = CDVPluginResult(
      status: CDVCommandStatus_ERROR
    )
    
      
      let storyboard = UIStoryboard(name: "GalpArMain", bundle: nil)
      let vc = storyboard.instantiateViewController(withIdentifier: "mainView")
      self.viewController.present(vc, animated: true)
      
      self.commandDelegate!.send(
      pluginResult,
      callbackId: command.callbackId
    )
  }
}
