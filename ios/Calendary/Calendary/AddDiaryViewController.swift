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
import Firebase
import FirebaseStorage
import Photos

class AddDiaryViewController: UIViewController, UITextViewDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var contentTextview: UITextView!
    @IBOutlet weak var titleTextfield: UITextField!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var contentTextviewBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    @IBOutlet weak var switchButton: UISwitch!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var onoffLabel: UILabel!
    @IBOutlet weak var addPhotoButton: UIButton!
    @IBOutlet weak var collectionView: UICollectionView!
    
    var date: Date = Date()
    var diaryId: String = ""
    let picker = UIImagePickerController()
    var imageArray: [(UIImage, String)] = []
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return imageArray.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectionCell", for: indexPath) as! CollectionViewCell
        
        cell.collectionImageView.image = imageArray[indexPath.item].0
        
        return cell
    }
    
    
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
    
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        textViewSetupView()
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        if textView.text == "" {
            textViewSetupView()
        }
    }
    
    func textViewSetupView() {
        if contentTextview.text == "내용입력" {
            contentTextview.text = ""
            contentTextview.textColor = UIColor.black
        } else if contentTextview.text == "" {
            contentTextview.text = "내용입력"
            contentTextview.textColor = UIColor.lightGray
        }
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        titleTextfield.layer.borderWidth = 1
        titleTextfield.layer.borderColor = UIColor.lightGray.cgColor
        titleTextfield.layer.cornerRadius = 10
        
        contentTextview.layer.borderWidth = 1
        contentTextview.layer.borderColor = UIColor.lightGray.cgColor
        contentTextview.layer.cornerRadius = 10
        
        contentTextview.delegate = self
        
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
//                }
//            }
//        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidShow(notification:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardDidHide(notification:)), name: UIResponder.keyboardWillHideNotification, object: nil)

        picker.delegate = self
        
        collectionView.delegate = self
        collectionView.dataSource = self
    }
    
    @IBAction func save(_ sender: Any) {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
                
        saveButton.isEnabled = false
                
        activityIndicatorView.startAnimating()
        
        var ref: DocumentReference? = nil
//        let calendar = Calendar.current
        let db = Firestore.firestore()
        let firebaseAuth = Auth.auth()
        guard titleTextfield.text?.isEmpty == false && contentTextview.text?.isEmpty == false else {
            activityIndicatorView.stopAnimating()
            let alertTitle = "내용을 입력하세요"
            
            let alertController = UIAlertController(title: alertTitle, message: nil, preferredStyle: .alert)
            let okButton = UIAlertAction(title: "확인", style: .default, handler: nil)
            alertController.addAction(okButton)
            self.present(alertController, animated: true, completion: nil)
            self.saveButton.isEnabled = true
            return
        }
        if diaryId == "" {
            ref = db.collection("Content").addDocument(data: [
                "content": contentTextview.text ?? "",
                "select timestamp": date,
                "title": titleTextfield.text ?? "",
                "timestamp": Date(),
                "user id": firebaseAuth.currentUser?.email,
                "show": switchButton.isOn,
                "image id": imageArray.map { $0.1 }.joined(separator: "|")
//                "user name": db.collection("User").whereField("Email", isEqualTo: firebaseAuth.currentUser?.email) as! String
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
//                    self.navigationController?.popViewController(animated: true)
                    self.dismiss(animated: true, completion: nil)
                })
                alertController.addAction(okButton)
                self.present(alertController, animated: true, completion: nil)
            }
            
            for image in imageArray {
                if let optimizedImageData = image.0.jpegData(compressionQuality: 0.6) {
                    uploadImage(imageData: optimizedImageData, uuid: image.1)
                }
            }
        } else {
            
        }
    }
    
    @IBAction func addPhoto(_ sender: Any) {
        let alert = UIAlertController(title: "어디서 사진을 가져올까", message: "골라줘", preferredStyle: .actionSheet)
        
        let library = UIAlertAction(title: "사진앨범", style: .default) {
            (action) in self.openLibrary()
        }
        
        let camera = UIAlertAction(title: "카메라", style: .default) {
            (action) in self.openCamera()
        }
        
        let cancel = UIAlertAction(title: "취소", style: .cancel, handler: nil)
        
        alert.addAction(library)
        alert.addAction(camera)
        alert.addAction(cancel)
        
        present(alert, animated: true, completion: nil)
        
    }
    
    func openLibrary() {
        picker.sourceType = .photoLibrary
        present(picker, animated: false, completion: nil)
    }
    
    func openCamera() {
        if (UIImagePickerController .isSourceTypeAvailable(.camera)) {
            picker.sourceType = .camera
            picker.modalPresentationStyle = .overFullScreen
            present(picker, animated: false, completion: nil)
        } else {
            print("Camera not available")
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
//            uploadImage(imageData: optimizedImageData)
            let uuid = NSUUID().uuidString
            self.imageArray.append((image, uuid))
            self.collectionView.reloadData()
            print(info)
        }
        dismiss(animated: true, completion: nil)
    }
    
    func uploadImage(imageData: Data, uuid: String) {
        let storageReference = Storage.storage().reference()
//        let currentUser = Auth.auth().currentUser
        
        let imageRef = storageReference.child("\(uuid)")
        
        let uploadMetaData = StorageMetadata()
        uploadMetaData.contentType = "image/jpeg"
        
        imageRef.putData(imageData, metadata: uploadMetaData) { (uploadMetaData, error) in
            if error != nil {
                print("Error took place \(String(describing: error?.localizedDescription))")
                return
            } else {
//                self.imageView1.image = UIImage(data: imageData)
                print("Meta data of uploaded image \(String(describing: uploadMetaData))")
            }
            
        }
    }
    
    
    @IBAction func cancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func backgroundClick(_ sender: UITapGestureRecognizer) {
        contentTextview.resignFirstResponder()
        titleTextfield.resignFirstResponder()
    }
    
    
    @IBAction func switchButtonClick(_ sender: Any) {
        if switchButton.isOn {
            onoffLabel.text = "공개"
        } else {
            onoffLabel.text = "비공개"
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

//extension AddDiaryViewController : UIImagePickerControllerDelegate, UINavigationControllerDelegate {
//
//    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
//        if let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
//            imageView1.image = image
//            print(info)
//        }
//        dismiss(animated: true, completion: nil)
//    }
//}
