//
//  SharePopoverViewController.swift
//  Calendary
//
//  Created by 김기현 on 2019/10/18.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class SharePopoverViewController: UIViewController {
    
    var delegate: ShareViewController?
    var segmentIndex = 0

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func newButton(_ sender: Any) {
        if segmentIndex == 1 {
            delegate?.method = .newSelected
        } else if segmentIndex == 2 {
            delegate?.method = .newWrite
        }
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func oldButton(_ sender: Any) {
        if segmentIndex == 1 {
            delegate?.method = .oldSelected
        } else if segmentIndex == 2 {
            delegate?.method = .oldWrite
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
