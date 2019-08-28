//
//  AddDiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 27/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore

class AddDiaryViewController: UIViewController {
    @IBOutlet weak var contentTextview: UITextView!
    @IBOutlet weak var titleTextfield: UITextField!
    @IBOutlet weak var saveButton: UIButton!
    
    var date: Date = Date()
    
    var diaryId: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleTextfield.layer.borderWidth = 1
        titleTextfield.layer.borderColor = UIColor.lightGray.cgColor
        titleTextfield.layer.cornerRadius = 10
        
        contentTextview.layer.borderWidth = 1
        contentTextview.layer.borderColor = UIColor.lightGray.cgColor
        contentTextview.layer.cornerRadius = 10
        
        saveButton.layer.cornerRadius = 10
        
        if diaryId != "" {
            let db = Firestore.firestore()
            let data = db.collection("diarys").document(diaryId)
            data.getDocument { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    if let title = querySnapshot!.get("title") as? String {
                        self.titleTextfield.text = title
                    }
                    if let content = querySnapshot!.get("content") as? String {
                        self.contentTextview.text = content
                    }
                }
            }
        }
        

        // Do any additional setup after loading the view.
    }
    
    @IBAction func backgroundClick(_ sender: UITapGestureRecognizer) {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
    }
    
    @IBAction func save() {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
        
        var ref: DocumentReference? = nil
        let calendar = Calendar.current
        let db = Firestore.firestore()
        ref = db.collection("diarys").addDocument(data: [
            "content": contentTextview.text ?? "",
            "date": "\(calendar.component(.year, from: date))/\(calendar.component(.month, from: date))/\(calendar.component(.day, from: date))",
            "title": titleTextfield.text ?? ""
        ]) { err in
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
