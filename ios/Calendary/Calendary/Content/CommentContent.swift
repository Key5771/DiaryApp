//
//  CommentContent.swift
//  Calendary
//
//  Created by 김기현 on 30/09/2019.
//  Copyright © 2019 김기현. All rights reserved.
//

import Foundation

struct CommentContent {
    var id: String
    var name: String
    var date: Date
    var content: String
}

extension CommentContent: Equatable {
    static func ==(lhs: CommentContent, rhs: CommentContent) -> Bool {
        return lhs.id == rhs.id
    }
}
