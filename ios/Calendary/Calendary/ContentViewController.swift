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
    
    var diaryId: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if diaryId != "" {
            let db = Firestore.firestore()
            let data = db.collection("diarys").document(diaryId)
            data.getDocument { (querySnapshot, err) in
                if let err = err {
                    print("Error getting documents: \(err)")
                } else {
                    if let title = querySnapshot!.get("title") as? String {
                        self.titleLabel.text = title
                    }
                    if let content = querySnapshot!.get("content") as? String {
                        self.contentTextview.text = content
                    }
                    if let date = querySnapshot!.get("date") as? String {
                        self.dateLabel.text = date
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
