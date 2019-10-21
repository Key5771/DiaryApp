//
//  Collection.swift
//  Calendary
//
//  Created by 김기현 on 2019/10/21.
//  Copyright © 2019 김기현. All rights reserved.
//

import Foundation

struct Collection {
    var imageId: String
    var imageName: String
}

extension Collection: Equatable {
    static func ==(lhs: Collection, rhs: Collection) -> Bool {
        return lhs.imageId == rhs.imageId
    }
}
