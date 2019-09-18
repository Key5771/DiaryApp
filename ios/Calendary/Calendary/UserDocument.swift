//
//  UserDocument.swift
//  Calendary
//
//  Created by 김기현 on 18/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import Foundation

struct UserDocument {
    var id: String
    var email: String
    var name: String
}

extension UserDocument: Equatable {
    static func ==(lhs: UserDocument, rhs: UserDocument) -> Bool {
        return lhs.id == rhs.id
    }
}
