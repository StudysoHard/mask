"use strict";
/**
 * @license
 * Copyright 2019 Google LLC. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================================================================
 */
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
exports.__esModule = true;
exports.BlazeFaceModel = exports.load = void 0;
var tfconv = require("@tensorflow/tfjs-converter");
var face_1 = require("./face");
var BLAZEFACE_MODEL_URL = 'https://tfhub.dev/tensorflow/tfjs-model/blazeface/1/default/1';
/**
 * Load blazeface.
 *
 * @param config A configuration object with the following properties:
 *  `maxFaces` The maximum number of faces returned by the model.
 *  `inputWidth` The width of the input image.
 *  `inputHeight` The height of the input image.
 *  `iouThreshold` The threshold for deciding whether boxes overlap too
 * much.
 *  `scoreThreshold` The threshold for deciding when to remove boxes based
 * on score.
 */
function load(_a) {
    var _b = _a === void 0 ? {} : _a, _c = _b.maxFaces, maxFaces = _c === void 0 ? 10 : _c, _d = _b.inputWidth, inputWidth = _d === void 0 ? 128 : _d, _e = _b.inputHeight, inputHeight = _e === void 0 ? 128 : _e, _f = _b.iouThreshold, iouThreshold = _f === void 0 ? 0.3 : _f, _g = _b.scoreThreshold, scoreThreshold = _g === void 0 ? 0.75 : _g, modelUrl = _b.modelUrl;
    return __awaiter(this, void 0, void 0, function () {
        var blazeface, localStorageHandler, e_1, model;
        return __generator(this, function (_h) {
            switch (_h.label) {
                case 0:
                    localStorageHandler = getApp().globalData.localStorageIO('blazeface_model');
                    _h.label = 1;
                case 1:
                    _h.trys.push([1, 3, , 8]);
                    console.log("load model from local storage");
                    return [4 /*yield*/, tfconv.loadGraphModel(localStorageHandler)];
                case 2:
                    blazeface = _h.sent();
                    return [3 /*break*/, 8];
                case 3:
                    e_1 = _h.sent();
                    console.log(e_1);
                    if (!(modelUrl != null)) return [3 /*break*/, 5];
                    console.log("load model from ".concat(modelUrl));
                    return [4 /*yield*/, tfconv.loadGraphModel(modelUrl)];
                case 4:
                    blazeface = _h.sent();
                    return [3 /*break*/, 7];
                case 5:
                    console.log("load model from ".concat(BLAZEFACE_MODEL_URL));
                    return [4 /*yield*/, tfconv.loadGraphModel(BLAZEFACE_MODEL_URL, {
                            fromTFHub: true
                        })];
                case 6:
                    blazeface = _h.sent();
                    _h.label = 7;
                case 7:
                    blazeface.save(localStorageHandler);
                    return [3 /*break*/, 8];
                case 8:
                    model = new face_1.BlazeFaceModel(blazeface, inputWidth, inputHeight, maxFaces, iouThreshold, scoreThreshold);
                    return [2 /*return*/, model];
            }
        });
    });
}
exports.load = load;
var face_2 = require("./face");
__createBinding(exports, face_2, "BlazeFaceModel");
