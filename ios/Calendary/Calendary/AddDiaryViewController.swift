//
//  AddDiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 27/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore
import FirebaseAuth

class AddDiaryViewController: UIViewController {
    @IBOutlet weak var contentTextview: UITextView!
    @IBOutlet weak var titleTextfield: UITextField!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var contentTextviewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    @IBOutlet weak var switchButton: UISwitch!
    
    var date: Date = Date()
    
    var diaryId: String = ""
    var switchText: String = ""
    
    @objc func keyboardDidShow(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue
        let height = (keyboardFrame.height)
        contentTextview.setContentOffset(CGPoint(x: 0, y: height/2), animated: true)
        contentTextviewBottomConstraint.constant = height
    }
    
    @objc func keyboardDidHide(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue
        let height = (keyboardFrame.height)
        contentTextviewBottomConstraint.constant = 32
        contentTextview.scrollIndicatorInsets.bottom -= height
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleTextfield.layer.borderWidth = 1
        titleTextfield.layer.borderColor = UIColor.lightGray.cgColor
        titleTextfield.layer.cornerRadius = 10
        
        contentTextview.layer.borderWidth = 1
        contentTextview.layer.borderColor = UIColor.lightGray.cgColor
        contentTextview.layer.cornerRadius = 10
        
        let calendar = Calendar.current
        dateLabel.text = "\(calendar.component(.year, from: date))년 \(calendar.component(.month, from: date))월 \(calendar.component(.day, from: date))일"
        
//        if diaryId != "" {
//            let db = Firestore.firestore()
//            let data = db.collection("diarys").document(diaryId)
//            data.getDocument { (querySnapshot, err) in
//                if let err = err {
//                    print("Error getting documents: \(err)")
//                } else {
//                    if let title = querySnapshot!.get("title") as? String {
//                        self.titleTextfield.text = title
//                    }
//                    if let content = querySnapshot!.get("content") as? String {
//                        self.contentTextview.text = content
//                    }
////                    if let date = querySnapshot!.get("date") as? String {
////                        self.contentTextview.text = content
////                    }
//                }
//            }
//        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)

        // Do any additional setup after loading the view.
    }
    
    @IBAction func backgroundClick(_ sender: UITapGestureRecognizer) {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
    }
    
    @IBAction func save() {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
        
        saveButton.isEnabled = false
        
        activityIndicatorView.startAnimating()
        
        var ref: DocumentReference? = nil
//        let calendar = Calendar.current
        let db = Firestore.firestore()
        let firebaseAuth = Auth.auth()
        if diaryId == "" {
            ref = db.collection("Content").addDocument(data: [
                "content": contentTextview.text ?? "",
                "select timestamp": date,
                "title": titleTextfield.text ?? "",
                "timestamp": Date(),
                "user id": firebaseAuth.currentUser?.email
            ]) { err in
                self.activityIndicatorView.stopAnimating()
                var alertTitle = "저장되었습니다."
                var alertMessage = "성공적으로 저장되었습니다."
                if err != nil {
                    alertTitle = "실패하였습니다."
                    alertMessage = "저장에 실패하였습니다."
                }
                
                let alertController = UIAlertController(title: alertTitle, message: alertMessage, preferredStyle: .alert)
                let okButton = UIAlertAction(title: "확인", style: .default, handler: { (_) in
                    self.navigationController?.popViewController(animated: true)
                })
                alertController.addAction(okButton)
                self.present(alertController, animated: true, completion: nil)
                
            }
        } else {
            
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
