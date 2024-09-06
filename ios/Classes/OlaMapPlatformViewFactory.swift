//
//  OlaMapPlatformViewFactory.swift
//  ola_maps_flutter_plugin
//
//  Created by Biswajit Paul on 06/09/24.
//

import Flutter
import UIKit
import OlaMapCore
import Foundation

class OlaMapPlatformViewFactory: NSObject, FlutterPlatformViewFactory {
    let messenger : FlutterBinaryMessenger

    init(messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
    }
    func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return OlaMapViewController(frame: frame, viewId: viewId, args: args , messenger: messenger)
    }
}

class OlaMapViewController: NSObject, FlutterPlatformView,OlaMapServiceDelegate {
    
    let frame: CGRect
    let viewId: Int64
    let messenger: FlutterBinaryMessenger

    var olaMap: OlaMapService
    var channel: FlutterMethodChannel

    init(frame: CGRect, viewId: Int64, args: Any?,messenger : FlutterBinaryMessenger) {
        self.frame = frame
        self.viewId = viewId
        self.messenger = messenger
        olaMap = OlaMapService(
            auth: .apiKey(key: "QXf3Ej8J6fpZAe58vxXl4uL5bNBL2CEVf5X34TPE"),  // Ensure this matches the expected method
            tileURL: URL(string: "https://api.olamaps.io/tiles/vector/v1/styles/default-light-standard/style.json")!, // Use URL(string:) initializer
            projectId: "343e0c32-3509-4a73-ab20-17c4bcc4f6ce"
        )
        self.channel = FlutterMethodChannel(name: "ola_maps_flutter_plugin_"+String(viewId), binaryMessenger: self.messenger)
        super.init()
        
    }

    func view() -> UIView {
        let viewController = UIViewController()
                    olaMap.loadMap(onView: viewController.view)
                    olaMap.setCurrentLocationMarkerColor(UIColor.systemBlue)
                    olaMap.setDebugLogs(true)
                    olaMap.delegate = self
        channel.setMethodCallHandler {   (call, result) in
            if call.method == "showCurrentLocation" {
                self.olaMap.setCamera(at: OlaCoordinate(latitude: 12.93177, longitude: 77.616370000000003), zoomLevel: 16.0)
//                if let args = call.arguments as? [String: Any], let data = args["data"] as? String {
//                    print("Received data from Flutter: \(data)")
//                    result(nil)
//                } else {
//                    result(FlutterError(code: "INVALID_ARGUMENT", message: "Data not provided", details: nil))
//                }
            }
        }
                return viewController.view
    }
    
    func didChangeCamera() {
        
    }
    
    func regionIsChanging(_ gesture: OlaMapCore.OlaMapGesture) {
        
    }
    
    func mapFailedToLoad(_ error: any Error) {
    
    }
    
    func didTapOnMap(_ coordinate: OlaCoordinate) {
        print(coordinate)
       
    }
    
    func mapSuccessfullyLoadedStyle() {
       print("Map Style Loaded")
    }
    
    func mapSuccessfullyLoaded() {
        print("Map Loaded")
        channel.invokeMethod("onMapReady", arguments: nil)
    }
    
    

}
