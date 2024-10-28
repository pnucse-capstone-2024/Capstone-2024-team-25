import BJFEZImg from "@assets/brands/BJFEZlogo.svg";
import BusanImg from "@assets/brands/Busanlogo.png";
import DepartmentImg from "@assets/brands/Departmentlogo.svg";
// import MinervaImg from "@assets/brands/Minervalogo.png";
import SCSAImg from "@assets/brands/SCSAlogo.png";
// import UCIImg from "@assets/brands/UCIlogo.svg";
// import UWAImg from "@assets/brands/UWAlogo.svg";
import mainImg from "@assets/main.png";
import Typewriter from 'typewriter-effect';

import Container from "./container";


function Hero() {
  return (
      <div className="bg-gray-200 bg-opacity-50"><Container className="flex flex-wrap">
      <div className="flex items-center w-full lg:w-1/2">
        <div className="w-full md:max-w-2xl mb-8">
        <h1 className="text-4xl font-bold leading-snug tracking-tight text-gray-800 lg:text-4xl lg:leading-tight xl:text-6xl xl:leading-tight" data-aos="zoom-y-out">
              TOPIK EXAM <br/> <span className="bg-clip-text text-transparent bg-gradient-to-r from-orange-500 to-teal-400" style={{ display: 'inline-block' }}>
                <Typewriter
                  options={{
                    strings: ['EASILY', 'FREELY', 'ANYWHERE', 'ANYTIME','SUCCESSFULLY'],
                    delay: 200,
                    deleteSpeed: 100,
                    autoStart: true,
                    loop: true,
                  }}
                />
              </span>
            </h1>
          <p className="py-5 text-xl leading-normal text-gray-500 lg:text-xl xl:text-2xl" data-aos="zoom-y-out" data-aos-delay="150">
          It is an educational platform that allows anyone, anywhere in the world, to take actual TOPIK tests by building up a question bank using AI technology.
          </p>

          <div className="flex flex-col sm:flex-row sm:items-center sm:space-x-4">
            <a
              href="http://www.vinedu.co.kr/mock/topik1/reading"
              target="_blank"
              rel="noopener"
              className="px-4 py-2 text-sm sm:text-xl font-medium text-center text-white bg-orange-400 hover:bg-orange-600 rounded-md transition duration-300 ease-in-out mb-3 sm:mb-0">
              TOPIK I
            </a>
            <a
              href="http://www.vinedu.co.kr/mock/topik2/reading"
              target="_blank"
              rel="noopener"
              className="px-4 py-2 text-sm sm:text-xl font-medium text-center text-white bg-teal-500 hover:bg-teal-700 rounded-md transition duration-300 ease-in-out mb-3 sm:mb-0">
              TOPIK II
            </a>
            <a
              href="http://www.vinedu.co.kr/mock/combination-exam"
              target="_blank"
              rel="noopener"
              className="px-4 py-2 text-sm sm:text-xl font-medium text-center text-white bg-purple-500 hover:bg-purple-700 rounded-md transition duration-300 ease-in-out mb-3 sm:mb-0">
              INTENSIVE
            </a>

            {/* <a
              href="http://tetori.iptime.org:5173/mock/combination-exam"
              target="_blank"
              rel="noopener"
              className="flex items-center px-4 py-2 text-sm sm:text-base space-x-2 text-gray-600 hover:text-gray-800">
              <svg
                role="img"
                width="20"
                height="20"
                className="w-4 h-4 sm:w-5 sm:h-5"
                viewBox="0 0 24 24"
                fill="currentColor"
                xmlns="http://www.w3.org/2000/svg">
                <title>Documents</title>
                <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 14H6v-2h6v2zm3-4H6v-2h9v2zm0-4H6V7h9v2z"/>
              </svg>
              <span>Additional Type By Mock Test</span>
            </a> */}
          </div>
        </div>
      </div>
      <div className="flex items-center justify-center w-full lg:w-1/2" data-aos="zoom-y-out" data-aos-delay="200">
        <div className="transform rounded-xl shadow-xl transition duration-300 hover:scale-105">
          <img
            src={mainImg}
            width="616"
            height="617"
            className="object-cover"
            alt="Hero Illustration" />
        </div>
      </div>
    </Container><Container>
        <div className="flex flex-col justify-center">
          <div className="text-xl text-center text-gray-700">
            Trusted by <span className="text-indigo-600">2000+</span>{" "}
            customers worldwide
          </div>

          {/* <div className="flex flex-wrap justify-center gap-5 mt-10 md:justify-around" data-aos="zoom-y-out" data-aos-delay="200">
            <div className="text-gray-400">
              <VanderbiltLogo />
            </div>
            <div className="mt-1 text-gray-400">
              <UCILogo />
            </div>
            <div className="text-gray-400">
              <WesternLogo />
            </div>
            <div className="-mt-3 text-gray-400">
              <MinervaLogo />
            </div>
            <div className=" text-gray-400">
              <SCSALogo />
            </div>
          </div> */}
          <div className="flex flex-wrap justify-center gap-5 mt-10 md:justify-around" data-aos="zoom-y-out" data-aos-delay="200">
            <div className="text-gray-400">
              <DepartmentLogo />
            </div>
            <div className=" text-gray-400">
              <SCSALogo />
            </div>
            <div className=" text-gray-400">
              <BusanLogo />
            </div>
            <div className="-mt-4 text-gray-400">
              <BJFEZLogo />
            </div>
          </div>
        </div>
      </Container></div>
  );
}

// function VanderbiltLogo() {
//   return <img src="https://upload.wikimedia.org/wikipedia/commons/d/d0/Vanderbilt_University_logo.svg" width={165} height={33}  alt="VanderbiltLogo" />;
// }

// function UCILogo() {
//   return <img src={UCIImg} width={210} height={33}  alt="UCILogo" />;
// }

// function WesternLogo() {
//   return <img src={UWAImg} width={140} height={63}  alt="WesternLogo" />;
// }

// function MinervaLogo() {
//   return <img src={MinervaImg} width={190} height={100}  alt="MinervaLogo" />;
// }

function DepartmentLogo() {
  return <img src={DepartmentImg} width={230} height={63}  alt="DepartmentLogo" />;
}

function SCSALogo() {
  return <img src={SCSAImg} width={200} height={100}  alt="SCSALogo" />;
}

function BusanLogo() {
  return <img src={BusanImg} width={220} height={100}  alt="BusanLogo" />;
}

function BJFEZLogo() {
  return <img src={BJFEZImg} width={190} height={100}  alt="BJFEZLogo" />;
}

export default Hero;