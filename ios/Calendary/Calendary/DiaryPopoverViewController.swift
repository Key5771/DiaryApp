//
//  DiaryPopoverViewController.swift
//  Calendary
//
//  Created by 김기현 on 04/10/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class DiaryPopoverViewController: UIViewController {

    
    var delegate: DiaryViewController?
    var segmentIndex = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
    }


    
    @IBAction func newSort(_ sender: Any) {
        if segmentIndex == 0 {
            delegate?.method = .newWrite
        } else if segmentIndex == 1 {
            delegate?.method = .newSelectTimestamp
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func oldSort(_ sender: Any) {
        if segmentIndex == 0 {
            delegate?.method = .oldWrite
        } else if segmentIndex == 1 {
            delegate?.method = .oldSelectTimestamp
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
