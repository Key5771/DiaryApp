//
//  ContentViewController.swift
//  Calendary
//
//  Created by 김기현 on 04/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import UIKit
import FirebaseFirestore
import FirebaseAuth

class ContentViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet var backgroundTap: UITapGestureRecognizer!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var userId: UILabel!
    @IBOutlet weak var dateLabel2: UILabel!
    @IBOutlet weak var contentLabel: UILabel!
    @IBOutlet var swipeGestureRecognizer: UISwipeGestureRecognizer!
    @IBOutlet weak var heart: UIImageView!
    @IBOutlet weak var likeCount: UILabel!
    @IBOutlet weak var commentTextField: UITextField!
    @IBOutlet weak var commentSendButton: UIButton!
    @IBOutlet weak var commentInputBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var scrollViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var commentTableView: UITableView!
    @IBOutlet weak var commentTableViewHeightConstraint: NSLayoutConstraint!
    
    var diaryId: String = ""
    var like: Bool = false
    var comment: [CommentContent] = []
    
    let db = Firestore.firestore()
    let firebaseAuth = Auth.auth()
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return comment.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = commentTableView.dequeueReusableCell(withIdentifier: "commentCell", for: indexPath) as! CommentTableViewCell
        cell.nameLabel.text = comment[indexPath.row].name
        cell.contentLabel.text = comment[indexPath.row].content
        
        let dateFormat: DateFormatter = DateFormatter()
        dateFormat.dateFormat = "yyyy/MM/dd/hh:mm"
        cell.timeLabel.text = dateFormat.string(from: comment[indexPath.row].date)
        
        return cell
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        commentTableView.delegate = self
        commentTableView.dataSource = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        if diaryId != "" {
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
                        self.contentLabel.text = content
                    }
                    
                    if let date = querySnapshot!.get("select timestamp") as? Timestamp {
                        let dateFormat: DateFormatter = DateFormatter()
                        dateFormat.dateFormat = "yyyy년 MM월 dd일"
                        let timestamp: String = dateFormat.string(from: date.dateValue())
                        self.dateLabel.text = timestamp
                    }
                    
                    if let user = querySnapshot!.get("user id") as? String {
                        self.userId.text = user
                    }
                    
                    if let currnetDate = querySnapshot?.get("timestamp") as? Timestamp {
                        let dateFormat: DateFormatter = DateFormatter()
                        dateFormat.dateFormat = "yyyy년 MM월 dd일 hh시 mm분"
                        let timestamp: String = dateFormat.string(from: currnetDate.dateValue())
                        self.dateLabel2.text = timestamp
                    }
                        
                    //                    if let date = querySnapshot!.get("date") as? String {
                    //                        self.contentTextview.text = content
                    //                    }
                }
            }
        }
        // 좋아요 한 경우와 안한 경우 구분해서 이미지 변경 및 변경되도록 설정
        db.collection("Content").document(diaryId).collection("Favorite").whereField("favUserId", isEqualTo: firebaseAuth.currentUser?.email).addSnapshotListener { (snapShot, err) in
            if err != nil {
                print("Error: \(err)")
            } else {
                if snapShot?.documents.isEmpty == true {
                    self.like = false
                    self.heart.image = UIImage(named: "heart")
                } else {
                    self.like = true
                    self.heart.image = UIImage(named: "like")
                }
            }
        }
        
        // addSnapshotListener -> 실시간 변경사항 변경되게 바꿔줌
        db.collection("Content").document(diaryId).collection("Favorite").addSnapshotListener { (snapShot, err) in
            if err != nil {
                print("Error: \(err)")
            } else {
                if let count = snapShot?.documents.count{
                    self.likeCount.text = "\(count)"
                } else {
                    self.likeCount.text = "0"
                }
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        getComment()
    }
    
    // 댓글 남긴 사용자의 정보와 댓글 내용 가져오기
    func getComment() {
        db.collection("Content").document(diaryId).collection("Comment").addSnapshotListener { (querySnapShot, err) in
            if err != nil {
                print("Error: \(err)")
            } else {
                self.comment = []
                
                for document in querySnapShot!.documents {
                    let commentContent: CommentContent = CommentContent(id: document.documentID, name: document.get("user id") as! String, date: (document.get("date") as! Timestamp).dateValue(), content: document.get("content") as! String)
                    self.comment.append(commentContent)
                }
                self.commentTableView.reloadData()
                self.commentTableViewHeightConstraint.constant = CGFloat(80 * self.comment.count)
            }
        }
    }
    
    @IBAction func sendButtonClick(_ sender: Any) {
        db.collection("Content").document(diaryId).collection("Comment").addDocument(data: [
            "user id": firebaseAuth.currentUser?.email,
            "content": commentTextField.text ?? "",
            "date": Date()
        ])
        commentTextField.text = ""
    }
    
    
    @IBAction func likeClick(_ sender: UITapGestureRecognizer) {
        
        if like == false {
            db.collection("Content").document(diaryId).collection("Favorite").addDocument(data: [
                "user id": firebaseAuth.currentUser?.email
            ])
        } else {
            db.collection("Content").document(diaryId).collection("Favorite").whereField("favUserId", isEqualTo: firebaseAuth.currentUser?.email).getDocuments { (snapshot, error) in
                snapshot?.documents.forEach { $0.reference.delete()}
            }
        }
    }
    
    
    @IBAction func backgroundTap(_ sender: UITapGestureRecognizer) {
        commentTextField.resignFirstResponder()
    }
    
    @objc func keyboardDidShow(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        var height = (keyboardFrame.height)
        print(height)
        if #available(iOS 11.0, *) {
            height -= self.view.safeAreaInsets.bottom
        }
        commentInputBottomConstraint.constant = height
        scrollViewBottomConstraint.constant = height
    }
    
    @objc func keyboardDidHide(notification: Notification) {
        let userInfo = notification.userInfo ?? [:]
        let keyboardFrame = (userInfo[UIResponder.keyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        let height = (keyboardFrame.height)
        commentInputBottomConstraint.constant = 0
        scrollViewBottomConstraint.constant = 0
    }
    
    
    @IBAction func cancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func swipe(_ sender: Any) {
        if swipeGestureRecognizer.state == .ended {
            self.dismiss(animated: true, completion: nil)
        } else if swipeGestureRecognizer.direction == .down {
            self.dismiss(animated: true, completion: nil)
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

