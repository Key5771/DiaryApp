//
//  ChangeInfoViewController.swift
//  Calendary
//
//  Created by 김기현 on 23/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit

class ChangeInfoViewController: UIViewController {
    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var emailTextfield: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    
    @IBAction func backgroundClick(_ sender: UITapGestureRecognizer) {
        nameTextfield.resignFirstResponder()
        emailTextfield.resignFirstResponder()
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
