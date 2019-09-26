//
//  PasswordFindViewController.swift
//  Calendary
//
//  Created by 김기현 on 29/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore
import FirebaseAuth

class PasswordFindViewController: UIViewController {
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        emailTextfield.layer.borderColor = UIColor(displayP3Red: 243/255, green: 177/255, blue: 90/255, alpha: 1).cgColor
        emailTextfield.layer.borderWidth = 1
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func send(_ sender: Any) {
        activityIndicator.startAnimating()
        let firebaseAuth = Auth.auth()
        firebaseAuth.sendPasswordReset(withEmail: emailTextfield.text ?? "") { error in
            self.activityIndicator.stopAnimating()
            var alertTitle = "이메일 발송"
            var alertMessage = "이메일을 보냈습니다!"
            if error != nil {
                alertTitle = "이메일 발송 실패"
                alertMessage = "메일 보내기 실패!"
            }
            
            let alertController = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
            let okButton = UIAlertAction(title: "확인", style: .default, handler: {(_) in
                self.navigationController?.popViewController(animated: true)
//                self.dismiss(animated: true, completion: nil)
            })
            alertController.addAction(okButton)
            self.present(alertController, animated: true, completion: nil)
        }
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
