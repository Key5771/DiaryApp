//
//  LoginViewController.swift
//  Calendary
//
//  Created by 김기현 on 29/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth
import GoogleSignIn

class LoginViewController: UIViewController, GIDSignInDelegate {
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var pwTextfield: UITextField!
    @IBOutlet weak var googleLogin: GIDSignInButton!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if let error = error {
            print(error)
            return
        }
        
        guard let authentication = user.authentication else { return }
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken, accessToken: authentication.accessToken)
        
        Auth.auth().signIn(with: credential) { (authResult, error) in
            if user != nil {
                print("Login Success")
                let viewController: UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "tabbar")
                viewController.modalPresentationStyle = .overFullScreen
                self.present(viewController, animated: true, completion: nil)
            } else {
                let alertController = UIAlertController(title: "로그인 실패", message: "로그인에 실패하였습니다", preferredStyle: .alert)
                let okButton = UIAlertAction(title: "확인", style: .default, handler: nil)
                
                alertController.addAction(okButton)
                self.present(alertController, animated: true, completion: nil)
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        GIDSignIn.sharedInstance()?.presentingViewController = self
        GIDSignIn.sharedInstance().clientID = FirebaseApp.app()?.options.clientID
        GIDSignIn.sharedInstance().delegate = self
        
        emailTextfield.layer.borderWidth = 1
        emailTextfield.layer.borderColor = UIColor(displayP3Red: 243/255, green: 177/255, blue: 90/255, alpha: 1).cgColor
//        emailTextfield.layer.cornerRadius = 5
        
        pwTextfield.layer.borderWidth = 1
        pwTextfield.layer.borderColor = UIColor(displayP3Red: 243/255, green: 177/255, blue: 90/255, alpha: 1).cgColor
//        pwTextfield.layer.cornerRadius = 5
        

        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.isNavigationBarHidden = true
        
        if Auth.auth().currentUser != nil {
            let viewController: UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "tabbar")
            viewController.modalPresentationStyle = .overFullScreen
            self.present(viewController, animated: true, completion: nil)
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.navigationController?.isNavigationBarHidden = false
        emailTextfield.text = ""
        pwTextfield.text = ""
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func loginButtonTouched(_ sender: Any) {
        
        activityIndicator.startAnimating()
        
        Auth.auth().signIn(withEmail: emailTextfield.text!, password: pwTextfield.text!) { (user, error) in

            if user != nil {
                self.activityIndicator.stopAnimating()
                print("Login Success")
                
                let viewController: UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "tabbar")
                viewController.modalPresentationStyle = .overFullScreen
                self.present(viewController, animated: true, completion: nil)
            } else {
                self.activityIndicator.stopAnimating()
                let alertController = UIAlertController(title: "로그인 실패", message: "로그인에 실패하였습니다", preferredStyle: .alert)
                let okButton = UIAlertAction(title: "확인", style: .default, handler: nil)

                alertController.addAction(okButton)
                self.present(alertController, animated: true, completion: nil)
            }
        }
    }
    
    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
//    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//        // Get the new view controller using segue.destination.
//        // Pass the selected object to the new view controller.
//
//
//    }
 

}
