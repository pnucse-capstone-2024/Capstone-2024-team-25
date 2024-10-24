
import featureImgOne from "@assets/features-bg.jpg";
import featureImgTwo from "@assets/features-bg2.jpg";

import Container from "./container";

function FeatureImg(){
    return (
        <Container className="flex flex-wrap mb-20 lg:gap-10 lg:flex-nowrap">
        <div
              className="flex items-center justify-center w-full lg:w-1/2">
              <div>
                <hr className="my-4 border-gray-700" style={{ borderWidth: '1.5px' }}/>
                <p className="text-xl text-gray-700 mb-4">
                  온라인 테스트 <span className='font-light text-gray-600'>Computer Based Test</span>
                </p>
                <div className="transform rounded-xl shadow-xl transition duration-300 hover:scale-105">
                  <img
                      src={featureImgOne}
                      alt="featureImgOne"
                      className="object-cover"
                    />
                </div>
              </div>
            </div>
            <div
              className="flex items-center justify-center w-full lg:w-1/2">
              <div>
                <hr className="my-4 border-gray-700" style={{ borderWidth: '1.5px' }}/>
                <p className="text-xl text-gray-700 mb-4">
                  선택형 테스트 <span className='font-light text-gray-600'>INTENSIVE</span>
                </p>
                <div className="transform rounded-xl shadow-xl transition duration-300 hover:scale-105">
                  <img
                      src={featureImgTwo}
                      alt="featureImgTwo"
                      className="object-cover"
                    />
                </div>
              </div>
            </div>
        </Container>
    );
}

export default FeatureImg;