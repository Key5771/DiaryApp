//
//  LoginViewController.swift
//  Calendary
//
//  Created by 김기현 on 29/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import Firebase
import GoogleSignIn

class LoginViewController: UIViewController {
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var pwTextfield: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        emailTextfield.layer.borderWidth = 1
        emailTextfield.layer.borderColor = UIColor(displayP3Red: 243/255, green: 177/255, blue: 90/255, alpha: 1).cgColor
        emailTextfield.layer.cornerRadius = 5
        
        pwTextfield.layer.borderWidth = 1
        pwTextfield.layer.borderColor = UIColor(displayP3Red: 243/255, green: 177/255, blue: 90/255, alpha: 1).cgColor
        pwTextfield.layer.cornerRadius = 5

        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.isNavigationBarHidden = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.isNavigationBarHidden = false
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
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
