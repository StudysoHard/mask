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
exports.BlazeFaceModel = void 0;
var tf = require("@tensorflow/tfjs-core");
var ANCHORS_CONFIG = {
    'strides': [8, 16],
    'anchors': [2, 6]
};
// `NUM_LANDMARKS` is a fixed property of the model.
var NUM_LANDMARKS = 6;
function generateAnchors(width, height, outputSpec) {
    var anchors = [];
    for (var i = 0; i < outputSpec.strides.length; i++) {
        var stride = outputSpec.strides[i];
        var gridRows = Math.floor((height + stride - 1) / stride);
        var gridCols = Math.floor((width + stride - 1) / stride);
        var anchorsNum = outputSpec.anchors[i];
        for (var gridY = 0; gridY < gridRows; gridY++) {
            var anchorY = stride * (gridY + 0.5);
            for (var gridX = 0; gridX < gridCols; gridX++) {
                var anchorX = stride * (gridX + 0.5);
                for (var n = 0; n < anchorsNum; n++) {
                    anchors.push([anchorX, anchorY]);
                }
            }
        }
    }
    return anchors;
}
function decodeBounds(boxOutputs, anchors) {
    var boxStarts = tf.slice(boxOutputs, [0, 1], [-1, 2]);
    var centers = tf.add(boxStarts, anchors);
    var boxSizes = tf.slice(boxOutputs, [0, 3], [-1, 2]);
    var halfBoxSize = tf.div(boxSizes, 2);
    var starts = tf.sub(centers, halfBoxSize);
    var ends = tf.add(centers, halfBoxSize);
    var concatAxis = 1;
    return tf.concat2d([starts, ends], concatAxis);
}
var BlazeFaceModel = /** @class */ (function () {
    function BlazeFaceModel(model, width, height, maxFaces, iouThreshold, scoreThreshold) {
        this.blazeFaceModel = model;
        this.width = width;
        this.height = height;
        this.maxFaces = maxFaces;
        this.anchorsData = generateAnchors(width, height, ANCHORS_CONFIG);
        this.anchors = tf.tensor2d(this.anchorsData);
        this.iouThreshold = iouThreshold;
        this.scoreThreshold = scoreThreshold;
        this.offsetX = 0;
        this.offsetY = 0;
        this.scaleFactor = 1;
    }
    BlazeFaceModel.prototype.makeSquare = function (image) {
        var start = [0, 0, 0];
        var size = [-1, -1, -1];
        var _a = image.shape, height = _a[0], width = _a[1], _ = _a[2];
        if (height > width) {
            this.offsetY = start[0] = (height - width) / 2;
            size[0] = width;
            this.scaleFactor = width / this.width;
        }
        else {
            this.offsetX = start[1] = (width - height) / 2;
            size[1] = height;
            this.scaleFactor = height / this.height;
        }
        return tf.slice(image, start, size);
    };
    BlazeFaceModel.prototype.preprocess = function (image) {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            return __generator(this, function (_a) {
                return [2 /*return*/, tf.tidy(function () {
                        // 把uint8数据变成tensor数据
                        var tensor3dImage = tf.browser.fromPixels(image);
                        // 截取图片中间正方形部分
                        var squareImage = _this.makeSquare(tensor3dImage);
                        // 缩小图片到 128*128
                        var resizedImage = tf.image.resizeBilinear(squareImage, [_this.width, _this.height]);
                        // 图片添加一个纬度（batch_size），用于适应预测输入要求
                        var tensor4dImage = tf.expandDims(resizedImage, 0);
                        // 图片从[0,255]归一化到[-1,1]
                        // int[0,255] -cast-> float[0,255] -div-> float[0,2] -sub-> float[-1,1]
                        var normalizedImage = tf.sub(tf.div(tf.cast(tensor4dImage, 'float32'), 127.5), 1);
                        return normalizedImage;
                    })];
            });
        });
    };
    BlazeFaceModel.prototype.postprocess = function (res, returnLandmark) {
        return __awaiter(this, void 0, void 0, function () {
            var _a, outputs, boxes, scores, indicesTensor, indices, _b, topLefts, bottomRights, score, landmarks, normalizedFaces, _loop_1, i;
            var _this = this;
            return __generator(this, function (_c) {
                switch (_c.label) {
                    case 0:
                        _a = tf.tidy(function () {
                            var prediction = tf.squeeze(res);
                            var decodedBounds = decodeBounds(prediction, _this.anchors);
                            var logits = tf.slice(prediction, [0, 0], [-1, 1]);
                            var scores = tf.squeeze(tf.sigmoid(logits));
                            return [prediction, decodedBounds, scores];
                        }), outputs = _a[0], boxes = _a[1], scores = _a[2];
                        return [4 /*yield*/, tf.image.nonMaxSuppressionAsync(boxes, scores, this.maxFaces, this.iouThreshold, this.scoreThreshold)];
                    case 1:
                        indicesTensor = _c.sent();
                        indices = indicesTensor.arraySync();
                        _b = tf.tidy(function () {
                            var suppressBox = tf.gather(boxes, indicesTensor);
                            var topLefts = tf.slice(suppressBox, [0, 0], [-1, 2])
                                .mul(_this.scaleFactor).add(tf.tensor1d([_this.offsetX, _this.offsetY]));
                            var bottomRights = tf.slice(suppressBox, [0, 2], [-1, 2])
                                .mul(_this.scaleFactor).add(tf.tensor1d([_this.offsetX, _this.offsetY]));
                            var suppressScore = tf.gather(scores, indicesTensor);
                            if (returnLandmark) {
                                var suppressOutput = tf.gather(outputs, indicesTensor);
                                var landmarks_1 = tf.slice(suppressOutput, [0, 5], [-1, -1])
                                    .reshape([-1, NUM_LANDMARKS, 2]);
                                return [topLefts.arraySync(), bottomRights.arraySync(), suppressScore.arraySync(), landmarks_1.arraySync()];
                            }
                            return [topLefts.arraySync(), bottomRights.arraySync(), suppressScore.arraySync(), []];
                        }), topLefts = _b[0], bottomRights = _b[1], score = _b[2], landmarks = _b[3];
                        // 删除没用的张量 防止内存泄漏
                        outputs.dispose();
                        boxes.dispose();
                        scores.dispose();
                        normalizedFaces = [];
                        _loop_1 = function (i) {
                            var normalizedFace = {
                                topLeft: topLefts[i],
                                bottomRight: bottomRights[i],
                                probability: score[i]
                            };
                            if (returnLandmark) {
                                var normalizedLandmark = (landmarks[i]).map(function (landmark) { return ([
                                    (landmark[0] + _this.anchorsData[indices[i]][0]) * _this.scaleFactor + _this.offsetX,
                                    (landmark[1] + _this.anchorsData[indices[i]][1]) * _this.scaleFactor + _this.offsetY
                                ]); });
                                normalizedFace.landmarks = normalizedLandmark;
                            }
                            normalizedFaces.push(normalizedFace);
                        };
                        for (i in indices) {
                            _loop_1(i);
                        }
                        indicesTensor.dispose();
                        return [2 /*return*/, normalizedFaces];
                }
            });
        });
    };
    BlazeFaceModel.prototype.estimateFaces = function (image, width, height, returnLandmark) {
        if (returnLandmark === void 0) { returnLandmark = true; }
        return __awaiter(this, void 0, void 0, function () {
            var preprocessImage, batchedPrediction, result;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, this.preprocess({ data: image, width: width, height: height })];
                    case 1:
                        preprocessImage = _a.sent();
                        return [4 /*yield*/, this.blazeFaceModel.predict(preprocessImage)];
                    case 2:
                        batchedPrediction = _a.sent();
                        preprocessImage.dispose();
                        result = this.postprocess(batchedPrediction, returnLandmark);
                        batchedPrediction.dispose();
                        return [2 /*return*/, result];
                }
            });
        });
    };
    return BlazeFaceModel;
}());
exports.BlazeFaceModel = BlazeFaceModel;
