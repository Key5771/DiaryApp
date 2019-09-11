//
//  JoinViewController.swift
//  Calendary
//
//  Created by 김기현 on 29/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import Firebase
import FirebaseAuth

class JoinViewController: UIViewController {
    @IBOutlet weak var nickNameTextfield: UITextField!
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var pwTextfield: UITextField!
    @IBOutlet weak var pwOkTextfield: UITextField!
    @IBOutlet weak var scrollViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var scrollView: UIScrollView!
    
    var joinId: String = ""
    
    @IBAction func signUpAction(_ sender: Any) {
        doSignUp()
    }
    
    @IBAction func gesture(_ sender: UITapGestureRecognizer) {
        print("touch")
        scrollView.endEditing(true)
    }
    
    @objc func keyboardDidShow(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue
        let height = (keyboardFrame.height)
        scrollView.setContentOffset(CGPoint(x: 0, y: height/2), animated: true)
        scrollViewBottomConstraint.constant = height
        scrollView.scrollIndicatorInsets.bottom += height
    }
    
    @objc func keyboardDidHide(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue
        let height = (keyboardFrame.height)
        scrollViewBottomConstraint.constant = 0
        scrollView.scrollIndicatorInsets.bottom -= height
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
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

extension JoinViewController {
    func showAlert(message: String) {
        let alert = UIAlertController(title: "회원가입 실패", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "확인", style: UIAlertAction.Style.default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func doSignUp() {
//        if nickNameTextfield.text! == "" {
//            showAlert(message: "닉네임")
//            return
//        }
        
        if emailTextfield.text! == "" {
            showAlert(message: "이메일")
            return
        }
        
        if pwTextfield.text! == "" {
            showAlert(message: "비밀번호")
            return
        }
        
        if pwOkTextfield.text! == "" {
            showAlert(message: "비밀번호 확인")
            return
        }
        
        signUp(email: emailTextfield.text!, password: pwTextfield.text!, passwordConfirm: pwOkTextfield.text!)
    }
    
    func signUp(email: String, password: String, passwordConfirm: String) {
        if password != passwordConfirm {
            self.showAlert(message: "비밀번호가 다릅니다.")
            return
        }
        Auth.auth().createUser(withEmail: email, password: password, completion: {(user, error) in
            if error != nil {
                if let errorCode = AuthErrorCode(rawValue: (error?._code)!) {
                    switch errorCode {
                    case AuthErrorCode.invalidEmail:
                        self.showAlert(message: "유효하지 않은 이메일입니다.")
                        
                    case AuthErrorCode.emailAlreadyInUse:
                        self.showAlert(message: "이미 가입한 회원입니다.")
                        
                    case AuthErrorCode.weakPassword:
                        self.showAlert(message: "비밀번호는 6자리 이상 입력해주세요.")
                        
                    case AuthErrorCode.wrongPassword:
                        self.showAlert(message: "비밀번호가 다릅니다.")
                        
                    default:
                        print(errorCode)
                    }
                }
            } else {
                print("회원가입 성공")
                
                   
                
                dump(user)
            }
        })
    }
}
