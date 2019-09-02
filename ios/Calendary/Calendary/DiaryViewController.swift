//
//  DiaryViewController.swift
//  Calendary
//
//  Created by 김기현 on 28/08/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore

class DiaryViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var diarys: [DiaryContent] = []
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return diarys.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "diaryCell", for: indexPath) as! DiaryTableViewCell
        cell.titleLabel.text = diarys[indexPath.row].title
        cell.dateLabel.text = diarys[indexPath.row].date
        return cell
    }

    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self

        let db = Firestore.firestore()
        
        db.collection("diarys").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    let diaryContent: DiaryContent = DiaryContent(id: document.documentID, title: document.get("title") as! String, date: document.get("date") as! String)
                    self.diarys.append(diaryContent)
                }
                
                self.tableView.reloadData()
            }
        }
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        imageView.contentMode = .scaleAspectFill
        let image = UIImage(named: "CalendaryLogo.png")
        imageView.image = image
        navigationItem.titleView = imageView
        
        // Do any additional setup after loading the view.
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "editDiary" {
            if let row = tableView.indexPathForSelectedRow {
                let vc = segue.destination as? AddDiaryViewController
                vc?.diaryId = diarys[row.row].id
                tableView.deselectRow(at: row, animated: true)
            }
        }
    }
 

}
