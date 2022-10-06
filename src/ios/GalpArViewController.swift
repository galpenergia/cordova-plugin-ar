//
//  ViewController.swift
//  testes 3
//
//  Created by Ronélio Oliveira on 06/10/22.
//

import UIKit
import RealityKit
import SceneKit

class GalpArViewController: UIViewController {
    
    @IBOutlet var arView: ARView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Load the "Box" scene from the "Experience" Reality File
        let boxAnchor = try! GalpArExperience.loadBox()
        
        guard let url = Bundle.main.url(forResource: "udz", withExtension: "usdz") else {
            return
        }
        
        let entity = try? Entity.load(contentsOf: url)


///  boxAnchor.addChild(entity!)
        // Add the box anchor to the scene
        arView.scene.anchors.append(boxAnchor)
        arView.scene.anchors.first?.addChild(entity!)
        entity!.availableAnimations.forEach { if #available(iOS 15.0, *) {
            entity?.playAnimation($0.repeat())
        } else {
            print("animation not available")
        } }
    }
}
