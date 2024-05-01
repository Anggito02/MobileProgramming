import os
import base64
import traceback
from datetime import datetime

from flask import Blueprint, jsonify, request

bp = Blueprint("api_bp", __name__)
STORAGE_PATH = os.path.join(os.getcwd(), "resources", "public")

@bp.route("/api/upload-photo", methods=["POST"])
def upload_photo():
    try:
        image = request.json["image"]
        timeNow = datetime.now().strftime("%Y%m%d-%H%M%S")

        with open(os.path.join(STORAGE_PATH, f"{timeNow}.png"), "wb") as f:
            f.write(base64.b64decode(image))

        return jsonify({
            'status': 'success',
            'message': 'Photo uploaded successfully'
        }), 200
    except:
        traceback.print_exc()
    return jsonify({
        'status': 'error',
        'message': 'Internal server error'
    }), 500