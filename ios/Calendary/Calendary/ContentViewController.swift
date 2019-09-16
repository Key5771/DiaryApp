//
//  ContentViewController.swift
//  Calendary
//
//  Created by 김기현 on 04/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore

class ContentViewController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var contentTextview: UITextView!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var userId: UILabel!
    
    var diaryId: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        if diaryId != "" {
            let db = Firestore.firestore()
            let data = db.collection("Content").document(diaryId)
            data.getDocument { (querySnapshot, err) in
                UIApplication.shared.isNetworkActivityIndicatorVisible = false
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    if let title = querySnapshot!.get("title") as? String {
                        self.titleLabel.text = title
                    }
                    if let content = querySnapshot!.get("content") as? String {
                        self.contentTextview.text = content
                    }
                    if let date = querySnapshot!.get("select timestamp") as? Timestamp {
                        let dateFormat: DateFormatter = DateFormatter()
                        dateFormat.dateFormat = "yyyy년 MM월 dd일"
                        let timestamp: String = dateFormat.string(from: date.dateValue())
                        self.dateLabel.text = timestamp
                    if let user = querySnapshot!.get("user id") as? String {
                        self.userId.text = user
                    }
                    //                    if let date = querySnapshot!.get("date") as? String {
                    //                        self.contentTextview.text = content
                    //                    }
                }
            }
        }

        // Do any additional setup after loading the view.
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
}
